import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVisits, NewVisits } from '../visits.model';

export type PartialUpdateVisits = Partial<IVisits> & Pick<IVisits, 'id'>;

type RestOf<T extends IVisits | NewVisits> = Omit<T, 'visitdate'> & {
  visitdate?: string | null;
};

export type RestVisits = RestOf<IVisits>;

export type NewRestVisits = RestOf<NewVisits>;

export type PartialUpdateRestVisits = RestOf<PartialUpdateVisits>;

export type EntityResponseType = HttpResponse<IVisits>;
export type EntityArrayResponseType = HttpResponse<IVisits[]>;

@Injectable({ providedIn: 'root' })
export class VisitsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/visits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(visits: NewVisits): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visits);
    return this.http
      .post<RestVisits>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(visits: IVisits): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visits);
    return this.http
      .put<RestVisits>(`${this.resourceUrl}/${this.getVisitsIdentifier(visits)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(visits: PartialUpdateVisits): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visits);
    return this.http
      .patch<RestVisits>(`${this.resourceUrl}/${this.getVisitsIdentifier(visits)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVisits>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVisits[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVisitsIdentifier(visits: Pick<IVisits, 'id'>): number {
    return visits.id;
  }

  compareVisits(o1: Pick<IVisits, 'id'> | null, o2: Pick<IVisits, 'id'> | null): boolean {
    return o1 && o2 ? this.getVisitsIdentifier(o1) === this.getVisitsIdentifier(o2) : o1 === o2;
  }

  addVisitsToCollectionIfMissing<Type extends Pick<IVisits, 'id'>>(
    visitsCollection: Type[],
    ...visitsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const visits: Type[] = visitsToCheck.filter(isPresent);
    if (visits.length > 0) {
      const visitsCollectionIdentifiers = visitsCollection.map(visitsItem => this.getVisitsIdentifier(visitsItem)!);
      const visitsToAdd = visits.filter(visitsItem => {
        const visitsIdentifier = this.getVisitsIdentifier(visitsItem);
        if (visitsCollectionIdentifiers.includes(visitsIdentifier)) {
          return false;
        }
        visitsCollectionIdentifiers.push(visitsIdentifier);
        return true;
      });
      return [...visitsToAdd, ...visitsCollection];
    }
    return visitsCollection;
  }

  protected convertDateFromClient<T extends IVisits | NewVisits | PartialUpdateVisits>(visits: T): RestOf<T> {
    return {
      ...visits,
      visitdate: visits.visitdate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVisits: RestVisits): IVisits {
    return {
      ...restVisits,
      visitdate: restVisits.visitdate ? dayjs(restVisits.visitdate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVisits>): HttpResponse<IVisits> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVisits[]>): HttpResponse<IVisits[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
