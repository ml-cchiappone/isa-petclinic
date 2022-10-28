import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VetComponent } from '../list/vet.component';
import { VetDetailComponent } from '../detail/vet-detail.component';
import { VetUpdateComponent } from '../update/vet-update.component';
import { VetRoutingResolveService } from './vet-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vetRoute: Routes = [
  {
    path: '',
    component: VetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VetDetailComponent,
    resolve: {
      vet: VetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VetUpdateComponent,
    resolve: {
      vet: VetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VetUpdateComponent,
    resolve: {
      vet: VetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vetRoute)],
  exports: [RouterModule],
})
export class VetRoutingModule {}
