import { ITypes } from 'app/entities/types/types.model';
import { IHuman } from 'app/entities/human/human.model';
import { IVet } from 'app/entities/vet/vet.model';

export interface IPets {
  id: number;
  name?: string | null;
  type?: Pick<ITypes, 'id'> | null;
  human?: Pick<IHuman, 'id'> | null;
  vet?: Pick<IVet, 'id'> | null;
}

export type NewPets = Omit<IPets, 'id'> & { id: null };
