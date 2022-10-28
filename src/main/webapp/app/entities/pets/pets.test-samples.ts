import { IPets, NewPets } from './pets.model';

export const sampleWithRequiredData: IPets = {
  id: 82218,
  name: 'AGP Calidad',
};

export const sampleWithPartialData: IPets = {
  id: 88826,
  name: 'Papelería',
};

export const sampleWithFullData: IPets = {
  id: 86401,
  name: 'European Account Inversor',
};

export const sampleWithNewData: NewPets = {
  name: 'Hogar monitorizar BCEAO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
