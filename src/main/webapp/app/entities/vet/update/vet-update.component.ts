import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VetFormService, VetFormGroup } from './vet-form.service';
import { IVet } from '../vet.model';
import { VetService } from '../service/vet.service';

@Component({
  selector: 'jhi-vet-update',
  templateUrl: './vet-update.component.html',
})
export class VetUpdateComponent implements OnInit {
  isSaving = false;
  vet: IVet | null = null;

  editForm: VetFormGroup = this.vetFormService.createVetFormGroup();

  constructor(protected vetService: VetService, protected vetFormService: VetFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vet }) => {
      this.vet = vet;
      if (vet) {
        this.updateForm(vet);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vet = this.vetFormService.getVet(this.editForm);
    if (vet.id !== null) {
      this.subscribeToSaveResponse(this.vetService.update(vet));
    } else {
      this.subscribeToSaveResponse(this.vetService.create(vet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVet>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vet: IVet): void {
    this.vet = vet;
    this.vetFormService.resetForm(this.editForm, vet);
  }
}
