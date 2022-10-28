import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HumanComponent } from './list/human.component';
import { HumanDetailComponent } from './detail/human-detail.component';
import { HumanUpdateComponent } from './update/human-update.component';
import { HumanDeleteDialogComponent } from './delete/human-delete-dialog.component';
import { HumanRoutingModule } from './route/human-routing.module';

@NgModule({
  imports: [SharedModule, HumanRoutingModule],
  declarations: [HumanComponent, HumanDetailComponent, HumanUpdateComponent, HumanDeleteDialogComponent],
})
export class HumanModule {}
