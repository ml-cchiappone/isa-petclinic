import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVisits } from '../visits.model';
import { VisitsService } from '../service/visits.service';

@Injectable({ providedIn: 'root' })
export class VisitsRoutingResolveService implements Resolve<IVisits | null> {
  constructor(protected service: VisitsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVisits | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((visits: HttpResponse<IVisits>) => {
          if (visits.body) {
            return of(visits.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
