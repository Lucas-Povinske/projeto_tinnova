import { Routes } from '@angular/router';
import { VeiculosComponent } from './veiculos/veiculos.component';

// Definição das rotas da aplicação
export const routes: Routes = [
  { path: '', component: VeiculosComponent, pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];
