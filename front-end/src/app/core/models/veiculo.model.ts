export interface Veiculo {
  id?: number;
  veiculo: string;
  marca: string;
  ano: number;
  cor?: string;
  descricao?: string;
  vendido: boolean;
  created?: string;
  updated?: string;
}

export interface FiltroVeiculo {
  marca?: string;
  ano?: number;
  cor?: string;
}
