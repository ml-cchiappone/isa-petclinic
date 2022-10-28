import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { HumanFormService, HumanFormGroup } from './human-form.service';
import { IHuman } from '../human.model';
import { HumanService } from '../service/human.service';

@Component({
  selector: 'jhi-human-update',
  templateUrl: './human-update.component.html',
})
export class HumanUpdateComponent implements OnInit {
  isSaving = false;
  human: IHuman | null = null;

  editForm: HumanFormGroup = this.humanFormService.createHumanFormGroup();

  constructor(
    protected humanService: HumanService,
    protected humanFormService: HumanFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ human }) => {
      this.human = human;
      if (human) {
        this.updateForm(human);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const human = this.humanFormService.getHuman(this.editForm);
    if (human.id !== null) {
      this.subscribeToSaveResponse(this.humanService.update(human));
    } else {
      this.subscribeToSaveResponse(this.humanService.create(human));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHuman>>): void {
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

  protected updateForm(human: IHuman): void {
    this.human = human;
    this.humanFormService.resetForm(this.editForm, human);
  }
}
