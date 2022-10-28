import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPets, NewPets } from '../pets.model';

export type PartialUpdatePets = Partial<IPets> & Pick<IPets, 'id'>;

export type EntityResponseType = HttpResponse<IPets>;
export type EntityArrayResponseType = HttpResponse<IPets[]>;

@Injectable({ providedIn: 'root' })
export class PetsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pets: NewPets): Observable<EntityResponseType> {
    return this.http.post<IPets>(this.resourceUrl, pets, { observe: 'response' });
  }

  update(pets: IPets): Observable<EntityResponseType> {
    return this.http.put<IPets>(`${this.resourceUrl}/${this.getPetsIdentifier(pets)}`, pets, { observe: 'response' });
  }

  partialUpdate(pets: PartialUpdatePets): Observable<EntityResponseType> {
    return this.http.patch<IPets>(`${this.resourceUrl}/${this.getPetsIdentifier(pets)}`, pets, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPets>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPets[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPetsIdentifier(pets: Pick<IPets, 'id'>): number {
    return pets.id;
  }

  comparePets(o1: Pick<IPets, 'id'> | null, o2: Pick<IPets, 'id'> | null): boolean {
    return o1 && o2 ? this.getPetsIdentifier(o1) === this.getPetsIdentifier(o2) : o1 === o2;
  }

  addPetsToCollectionIfMissing<Type extends Pick<IPets, 'id'>>(
    petsCollection: Type[],
    ...petsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pets: Type[] = petsToCheck.filter(isPresent);
    if (pets.length > 0) {
      const petsCollectionIdentifiers = petsCollection.map(petsItem => this.getPetsIdentifier(petsItem)!);
      const petsToAdd = pets.filter(petsItem => {
        const petsIdentifier = this.getPetsIdentifier(petsItem);
        if (petsCollectionIdentifiers.includes(petsIdentifier)) {
          return false;
        }
        petsCollectionIdentifiers.push(petsIdentifier);
        return true;
      });
      return [...petsToAdd, ...petsCollection];
    }
    return petsCollection;
  }
}
