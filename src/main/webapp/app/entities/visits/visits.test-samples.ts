import dayjs from 'dayjs/esm';

import { IVisits, NewVisits } from './visits.model';

export const sampleWithRequiredData: IVisits = {
  id: 35290,
  visitdate: dayjs('2022-10-27T22:43'),
  description: 'Personal Berkshire state',
};

export const sampleWithPartialData: IVisits = {
  id: 83531,
  visitdate: dayjs('2022-10-28T04:14'),
  description: 'Seguro',
};

export const sampleWithFullData: IVisits = {
  id: 49530,
  visitdate: dayjs('2022-10-28T06:03'),
  description: 'Avon Informática',
};

export const sampleWithNewData: NewVisits = {
  visitdate: dayjs('2022-10-28T16:57'),
  description: 'GB',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
