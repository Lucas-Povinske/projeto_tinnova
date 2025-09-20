import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Veiculo } from '../models/veiculo.model';

@Injectable({ providedIn: 'root' })
export class VeiculosService {
  private readonly base = 'http://localhost:8080/veiculos';

  constructor(private http: HttpClient) {}

  // GET /veiculos?marca=&ano=&cor= (Todos os filtros são opcionais)
  list(params?: { marca?: string; ano?: number; cor?: string }): Observable<Veiculo[]> {

    let hp = new HttpParams();
    if (params?.marca) hp = hp.set('marca', params.marca);
    if (params?.ano != null) hp = hp.set('ano', params.ano);
    if (params?.cor) hp = hp.set('cor', params.cor);

    // Se não passar nenhum parametro, ele retorna todos os veículos
    return this.http.get<Veiculo[]>(this.base, { params: hp });
  }

  // GET /veiculos/{id}
  get(id: number): Observable<Veiculo> {
    return this.http.get<Veiculo>(`${this.base}/${id}`);
  }

  // POST /veiculos (Usa o mesmo DTO para criar)
  create(v: Veiculo): Observable<Veiculo> {
    return this.http.post<Veiculo>(this.base, v);
  }

  // PUT /veiculos/{id} (Atualiza todos os campos)
  update(id: number, v: Veiculo): Observable<Veiculo> {
    return this.http.put<Veiculo>(`${this.base}/${id}`, v);
  }

  // PATCH /veiculos/{id} (Atualização parcial)
  patch(id: number, parcial: Partial<Veiculo>): Observable<Veiculo> {
    return this.http.patch<Veiculo>(`${this.base}/${id}`, parcial);
  }

  // DELETE /veiculos/{id} (Remove o veículo)
  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}