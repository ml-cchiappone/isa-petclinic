export interface IHuman {
  id: number;
  firstname?: string | null;
  lastname?: string | null;
  address?: string | null;
  city?: string | null;
  telephone?: string | null;
}

export type NewHuman = Omit<IHuman, 'id'> & { id: null };
