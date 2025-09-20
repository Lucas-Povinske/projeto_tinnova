import { Component, OnInit, computed, effect, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { VeiculosService } from '../core/services/veiculos.service';
import { Veiculo } from '../core/models/veiculo.model';
import { AbstractControl, ValidationErrors } from '@angular/forms';

const MARCAS_VALIDAS = [
  'Volkswagen','Ford','Honda','Chevrolet','Fiat','Toyota',
  'Hyundai','Renault','Nissan','Peugeot','BMW','Mercedes-Benz','Audi','Kia'
];

@Component({
  standalone: true,
  selector: 'app-veiculos',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './veiculos.component.html',
  styleUrls: ['./veiculo.component.css']
})
export class VeiculosComponent implements OnInit {

  // Dataset completo vindo da API
  veiculos = signal<Veiculo[]>([]);
  carregando = signal<boolean>(false);
  editandoId = signal<number | null>(null);

  // Filtros da tabela (Não afetam as estatísticas)
  filtroMarca = signal<string>('');
  filtroAno = signal<number | null>(null);
  filtroCor = signal<string>('');

  form!: FormGroup;

  constructor(private fb: FormBuilder, private api: VeiculosService) {
    // Se estiver editando, preenche o formulário com os dados do veículo
    effect(() => {
      const id = this.editandoId();
      if (id == null) return;
      const v = this.veiculos().find(x => x.id === id);
      if (v) this.form.patchValue(v);
    });
  }

  // Validação customizada para marcas válidas
  marcaValidaValidator(marcasValidas: string[]) {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = (control.value || '').trim();
      if (!value) return null; // Required handled separately
      const isValid = marcasValidas.some(m => m.toLowerCase() === value.toLowerCase());
      return isValid ? null : { marcaInvalida: true };
    };
  }

  ngOnInit(): void { 
    // Inicializa o formulário
    this.form = this.fb.group({
    veiculo: ['', Validators.required],
    marca:   ['', [Validators.required, this.marcaValidaValidator(this.marcasValidas)]],
    ano:     [new Date().getFullYear(), [Validators.required, Validators.min(1886)]],
    cor:     [''],
    descricao: [''],
    vendido:  [false, Validators.required]
    });
    this.reload(); 
  }

  reload() {
    this.carregando.set(true);
    // Para o front, sempre recarrega a lista completa e filtra localmente
    this.api.list().subscribe({
      next: vs => this.veiculos.set(vs),
      complete: () => this.carregando.set(false)
    });
  }

  editar(v: Veiculo) {
    this.editandoId.set(v.id!);
    this.form.patchValue(v);
    window.scrollTo({ top: 0, behavior: 'smooth' }); // Rola para o topo da página
  }

  // Cancela a edição e reseta o formulário
  cancelarEdicao() {
    this.editandoId.set(null);
    this.form.reset({
      veiculo: '',
      marca: '',
      ano: new Date().getFullYear(),
      cor: '',
      descricao: '',
      vendido: false
    });
  }

  salvar() {
    const payload = this.form.value as unknown as Veiculo;
    const id = this.editandoId();

    const done = () => { this.cancelarEdicao(); this.reload(); };

    // Se estiver editando, faz update, senão cria novo
    if (id != null) this.api.update(id, payload).subscribe(done);
    else this.api.create(payload).subscribe(done);
  }

  excluir(id?: number) {
    if (!id) return;
    if (!confirm('Confirma exclusão?')) return; // Foi decidido pedir confirmação pelo browser por hora
    this.api.remove(id).subscribe(() => this.reload());
  }

  marcasValidas = MARCAS_VALIDAS;

  // Lista filtrada (apenas visual, não afeta as estatísticas)
  listaFiltrada = computed(() => {
    const marca = this.filtroMarca().trim().toLowerCase();
    const ano = this.filtroAno();
    const cor = this.filtroCor().trim().toLowerCase();
    return this.veiculos().filter(v =>
      (!marca || (v.marca || '').toLowerCase() === marca) &&
      (ano == null || v.ano === ano) &&
      (!cor || (v.cor || '').toLowerCase() === cor)
    ).sort((a,b) => (a.id ?? 0) - (b.id ?? 0));
  });

  // Estatísticas (Sempre sobre o dataset completo, é calculado no front por hora)
  naoVendidos = computed(() => this.veiculos().filter(v => !v.vendido).length);

  distrDecada = computed(() => {
    const map = new Map<number, number>();
    for (const v of this.veiculos()) {
      const dec = Math.floor(v.ano / 10) * 10;
      map.set(dec, (map.get(dec) ?? 0) + 1);
    }
    return Array.from(map.entries())
      .sort((a,b) => a[0]-b[0])
      .map(([dec, qt]) => ({ chave: `Década de ${dec}`, quantidade: qt }));
  });

  distrMarca = computed(() => {
    const map = new Map<string, number>();
    for (const v of this.veiculos()) {
      const k = (v.marca || '').trim().toLowerCase();
      if (!k) continue;
      map.set(k, (map.get(k) ?? 0) + 1);
    }
    return Array.from(map.entries())
      .sort((a,b) => a[0].localeCompare(b[0]))
      .map(([k, qt]) => ({ chave: k.replace(/\b\w/g, c => c.toUpperCase()), quantidade: qt }));
  });

  ultimaSemana = computed(() => {
    const d = new Date(); d.setDate(d.getDate() - 7);
    return this.veiculos().filter(v => v.created && new Date(v.created) >= d);
  });

  // Limpa os filtros da tabela
  limparFiltros() {
    this.filtroMarca.set('');
    this.filtroAno.set(null);
    this.filtroCor.set('');
  }

  // Adicionado para otimizar o ngFor da lista
  trackById(index: number, v: Veiculo): number | undefined {
    return v?.id;
  }
}
