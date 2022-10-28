export interface IVet {
  id: number;
  firstname?: string | null;
  lastname?: string | null;
}

export type NewVet = Omit<IVet, 'id'> & { id: null };
