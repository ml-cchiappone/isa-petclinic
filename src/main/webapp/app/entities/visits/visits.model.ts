import dayjs from 'dayjs/esm';
import { IPets } from 'app/entities/pets/pets.model';

export interface IVisits {
  id: number;
  visitdate?: dayjs.Dayjs | null;
  description?: string | null;
  pet?: Pick<IPets, 'id'> | null;
}

export type NewVisits = Omit<IVisits, 'id'> & { id: null };
