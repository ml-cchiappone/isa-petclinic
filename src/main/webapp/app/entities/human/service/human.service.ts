import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHuman, NewHuman } from '../human.model';

export type PartialUpdateHuman = Partial<IHuman> & Pick<IHuman, 'id'>;

export type EntityResponseType = HttpResponse<IHuman>;
export type EntityArrayResponseType = HttpResponse<IHuman[]>;

@Injectable({ providedIn: 'root' })
export class HumanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/humans');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(human: NewHuman): Observable<EntityResponseType> {
    return this.http.post<IHuman>(this.resourceUrl, human, { observe: 'response' });
  }

  update(human: IHuman): Observable<EntityResponseType> {
    return this.http.put<IHuman>(`${this.resourceUrl}/${this.getHumanIdentifier(human)}`, human, { observe: 'response' });
  }

  partialUpdate(human: PartialUpdateHuman): Observable<EntityResponseType> {
    return this.http.patch<IHuman>(`${this.resourceUrl}/${this.getHumanIdentifier(human)}`, human, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHuman>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHuman[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHumanIdentifier(human: Pick<IHuman, 'id'>): number {
    return human.id;
  }

  compareHuman(o1: Pick<IHuman, 'id'> | null, o2: Pick<IHuman, 'id'> | null): boolean {
    return o1 && o2 ? this.getHumanIdentifier(o1) === this.getHumanIdentifier(o2) : o1 === o2;
  }

  addHumanToCollectionIfMissing<Type extends Pick<IHuman, 'id'>>(
    humanCollection: Type[],
    ...humansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const humans: Type[] = humansToCheck.filter(isPresent);
    if (humans.length > 0) {
      const humanCollectionIdentifiers = humanCollection.map(humanItem => this.getHumanIdentifier(humanItem)!);
      const humansToAdd = humans.filter(humanItem => {
        const humanIdentifier = this.getHumanIdentifier(humanItem);
        if (humanCollectionIdentifiers.includes(humanIdentifier)) {
          return false;
        }
        humanCollectionIdentifiers.push(humanIdentifier);
        return true;
      });
      return [...humansToAdd, ...humanCollection];
    }
    return humanCollection;
  }
}
