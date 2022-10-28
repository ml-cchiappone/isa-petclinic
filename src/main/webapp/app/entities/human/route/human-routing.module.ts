import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HumanComponent } from '../list/human.component';
import { HumanDetailComponent } from '../detail/human-detail.component';
import { HumanUpdateComponent } from '../update/human-update.component';
import { HumanRoutingResolveService } from './human-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const humanRoute: Routes = [
  {
    path: '',
    component: HumanComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HumanDetailComponent,
    resolve: {
      human: HumanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HumanUpdateComponent,
    resolve: {
      human: HumanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HumanUpdateComponent,
    resolve: {
      human: HumanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(humanRoute)],
  exports: [RouterModule],
})
export class HumanRoutingModule {}
