import { IHuman, NewHuman } from './human.model';

export const sampleWithRequiredData: IHuman = {
  id: 7892,
  firstname: 'direccional backing',
  lastname: 'RSS Guinea Negro',
  address: 'Violeta Agente Dollar',
  telephone: '980 869 625',
};

export const sampleWithPartialData: IHuman = {
  id: 33398,
  firstname: 'XSS Loan AI',
  lastname: 'monitor Comunicaciones',
  address: 'clicks-and-mortar',
  city: 'María Soledadmouth',
  telephone: '995-869-212',
};

export const sampleWithFullData: IHuman = {
  id: 87009,
  firstname: 'acompasada Increible',
  lastname: 'bluetooth',
  address: 'Parafarmacia',
  city: 'Inca Juliaport',
  telephone: '992 922 889',
};

export const sampleWithNewData: NewHuman = {
  firstname: 'asimétrica Angola Amarillo',
  lastname: 'navigating SSL Cantabria',
  address: 'Técnico Gerente',
  telephone: '965 245 064',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
