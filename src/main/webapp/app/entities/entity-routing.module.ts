import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'vet',
        data: { pageTitle: 'petclinicApp.vet.home.title' },
        loadChildren: () => import('./vet/vet.module').then(m => m.VetModule),
      },
      {
        path: 'types',
        data: { pageTitle: 'petclinicApp.types.home.title' },
        loadChildren: () => import('./types/types.module').then(m => m.TypesModule),
      },
      {
        path: 'human',
        data: { pageTitle: 'petclinicApp.human.home.title' },
        loadChildren: () => import('./human/human.module').then(m => m.HumanModule),
      },
      {
        path: 'pets',
        data: { pageTitle: 'petclinicApp.pets.home.title' },
        loadChildren: () => import('./pets/pets.module').then(m => m.PetsModule),
      },
      {
        path: 'visits',
        data: { pageTitle: 'petclinicApp.visits.home.title' },
        loadChildren: () => import('./visits/visits.module').then(m => m.VisitsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
