import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

// Componente raiz, apenas um outlet para as rotas
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `<router-outlet></router-outlet>`
})
export class AppComponent {}
