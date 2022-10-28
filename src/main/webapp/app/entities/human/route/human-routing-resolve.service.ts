import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHuman } from '../human.model';
import { HumanService } from '../service/human.service';

@Injectable({ providedIn: 'root' })
export class HumanRoutingResolveService implements Resolve<IHuman | null> {
  constructor(protected service: HumanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHuman | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((human: HttpResponse<IHuman>) => {
          if (human.body) {
            return of(human.body);
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
