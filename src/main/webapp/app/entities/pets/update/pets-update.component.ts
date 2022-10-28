import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PetsFormService, PetsFormGroup } from './pets-form.service';
import { IPets } from '../pets.model';
import { PetsService } from '../service/pets.service';
import { ITypes } from 'app/entities/types/types.model';
import { TypesService } from 'app/entities/types/service/types.service';
import { IHuman } from 'app/entities/human/human.model';
import { HumanService } from 'app/entities/human/service/human.service';
import { IVet } from 'app/entities/vet/vet.model';
import { VetService } from 'app/entities/vet/service/vet.service';

@Component({
  selector: 'jhi-pets-update',
  templateUrl: './pets-update.component.html',
})
export class PetsUpdateComponent implements OnInit {
  isSaving = false;
  pets: IPets | null = null;

  typesSharedCollection: ITypes[] = [];
  humansSharedCollection: IHuman[] = [];
  vetsSharedCollection: IVet[] = [];

  editForm: PetsFormGroup = this.petsFormService.createPetsFormGroup();

  constructor(
    protected petsService: PetsService,
    protected petsFormService: PetsFormService,
    protected typesService: TypesService,
    protected humanService: HumanService,
    protected vetService: VetService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTypes = (o1: ITypes | null, o2: ITypes | null): boolean => this.typesService.compareTypes(o1, o2);

  compareHuman = (o1: IHuman | null, o2: IHuman | null): boolean => this.humanService.compareHuman(o1, o2);

  compareVet = (o1: IVet | null, o2: IVet | null): boolean => this.vetService.compareVet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pets }) => {
      this.pets = pets;
      if (pets) {
        this.updateForm(pets);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pets = this.petsFormService.getPets(this.editForm);
    if (pets.id !== null) {
      this.subscribeToSaveResponse(this.petsService.update(pets));
    } else {
      this.subscribeToSaveResponse(this.petsService.create(pets));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPets>>): void {
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

  protected updateForm(pets: IPets): void {
    this.pets = pets;
    this.petsFormService.resetForm(this.editForm, pets);

    this.typesSharedCollection = this.typesService.addTypesToCollectionIfMissing<ITypes>(this.typesSharedCollection, pets.type);
    this.humansSharedCollection = this.humanService.addHumanToCollectionIfMissing<IHuman>(this.humansSharedCollection, pets.human);
    this.vetsSharedCollection = this.vetService.addVetToCollectionIfMissing<IVet>(this.vetsSharedCollection, pets.vet);
  }

  protected loadRelationshipsOptions(): void {
    this.typesService
      .query()
      .pipe(map((res: HttpResponse<ITypes[]>) => res.body ?? []))
      .pipe(map((types: ITypes[]) => this.typesService.addTypesToCollectionIfMissing<ITypes>(types, this.pets?.type)))
      .subscribe((types: ITypes[]) => (this.typesSharedCollection = types));

    this.humanService
      .query()
      .pipe(map((res: HttpResponse<IHuman[]>) => res.body ?? []))
      .pipe(map((humans: IHuman[]) => this.humanService.addHumanToCollectionIfMissing<IHuman>(humans, this.pets?.human)))
      .subscribe((humans: IHuman[]) => (this.humansSharedCollection = humans));

    this.vetService
      .query()
      .pipe(map((res: HttpResponse<IVet[]>) => res.body ?? []))
      .pipe(map((vets: IVet[]) => this.vetService.addVetToCollectionIfMissing<IVet>(vets, this.pets?.vet)))
      .subscribe((vets: IVet[]) => (this.vetsSharedCollection = vets));
  }
}
