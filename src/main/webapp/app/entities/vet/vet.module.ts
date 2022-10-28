import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VetComponent } from './list/vet.component';
import { VetDetailComponent } from './detail/vet-detail.component';
import { VetUpdateComponent } from './update/vet-update.component';
import { VetDeleteDialogComponent } from './delete/vet-delete-dialog.component';
import { VetRoutingModule } from './route/vet-routing.module';

@NgModule({
  imports: [SharedModule, VetRoutingModule],
  declarations: [VetComponent, VetDetailComponent, VetUpdateComponent, VetDeleteDialogComponent],
})
export class VetModule {}
