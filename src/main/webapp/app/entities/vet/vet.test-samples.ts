import { IVet, NewVet } from './vet.model';

export const sampleWithRequiredData: IVet = {
  id: 99986,
  firstname: 'Tonga',
  lastname: 'invoice',
};

export const sampleWithPartialData: IVet = {
  id: 16949,
  firstname: 'connect',
  lastname: 'online',
};

export const sampleWithFullData: IVet = {
  id: 44422,
  firstname: 'Caserio deposit',
  lastname: 'Account Cantabria',
};

export const sampleWithNewData: NewVet = {
  firstname: 'Bedfordshire calculating Bucking',
  lastname: 'Administrador Hormigon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
