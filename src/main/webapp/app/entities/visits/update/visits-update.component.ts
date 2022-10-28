import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VisitsFormService, VisitsFormGroup } from './visits-form.service';
import { IVisits } from '../visits.model';
import { VisitsService } from '../service/visits.service';
import { IPets } from 'app/entities/pets/pets.model';
import { PetsService } from 'app/entities/pets/service/pets.service';

@Component({
  selector: 'jhi-visits-update',
  templateUrl: './visits-update.component.html',
})
export class VisitsUpdateComponent implements OnInit {
  isSaving = false;
  visits: IVisits | null = null;

  petsSharedCollection: IPets[] = [];

  editForm: VisitsFormGroup = this.visitsFormService.createVisitsFormGroup();

  constructor(
    protected visitsService: VisitsService,
    protected visitsFormService: VisitsFormService,
    protected petsService: PetsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePets = (o1: IPets | null, o2: IPets | null): boolean => this.petsService.comparePets(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visits }) => {
      this.visits = visits;
      if (visits) {
        this.updateForm(visits);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visits = this.visitsFormService.getVisits(this.editForm);
    if (visits.id !== null) {
      this.subscribeToSaveResponse(this.visitsService.update(visits));
    } else {
      this.subscribeToSaveResponse(this.visitsService.create(visits));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisits>>): void {
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

  protected updateForm(visits: IVisits): void {
    this.visits = visits;
    this.visitsFormService.resetForm(this.editForm, visits);

    this.petsSharedCollection = this.petsService.addPetsToCollectionIfMissing<IPets>(this.petsSharedCollection, visits.pet);
  }

  protected loadRelationshipsOptions(): void {
    this.petsService
      .query()
      .pipe(map((res: HttpResponse<IPets[]>) => res.body ?? []))
      .pipe(map((pets: IPets[]) => this.petsService.addPetsToCollectionIfMissing<IPets>(pets, this.visits?.pet)))
      .subscribe((pets: IPets[]) => (this.petsSharedCollection = pets));
  }
}
