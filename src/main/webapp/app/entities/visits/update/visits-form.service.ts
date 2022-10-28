import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVisits, NewVisits } from '../visits.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVisits for edit and NewVisitsFormGroupInput for create.
 */
type VisitsFormGroupInput = IVisits | PartialWithRequiredKeyOf<NewVisits>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVisits | NewVisits> = Omit<T, 'visitdate'> & {
  visitdate?: string | null;
};

type VisitsFormRawValue = FormValueOf<IVisits>;

type NewVisitsFormRawValue = FormValueOf<NewVisits>;

type VisitsFormDefaults = Pick<NewVisits, 'id' | 'visitdate'>;

type VisitsFormGroupContent = {
  id: FormControl<VisitsFormRawValue['id'] | NewVisits['id']>;
  visitdate: FormControl<VisitsFormRawValue['visitdate']>;
  description: FormControl<VisitsFormRawValue['description']>;
  pet: FormControl<VisitsFormRawValue['pet']>;
};

export type VisitsFormGroup = FormGroup<VisitsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VisitsFormService {
  createVisitsFormGroup(visits: VisitsFormGroupInput = { id: null }): VisitsFormGroup {
    const visitsRawValue = this.convertVisitsToVisitsRawValue({
      ...this.getFormDefaults(),
      ...visits,
    });
    return new FormGroup<VisitsFormGroupContent>({
      id: new FormControl(
        { value: visitsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      visitdate: new FormControl(visitsRawValue.visitdate, {
        validators: [Validators.required],
      }),
      description: new FormControl(visitsRawValue.description, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      pet: new FormControl(visitsRawValue.pet),
    });
  }

  getVisits(form: VisitsFormGroup): IVisits | NewVisits {
    return this.convertVisitsRawValueToVisits(form.getRawValue() as VisitsFormRawValue | NewVisitsFormRawValue);
  }

  resetForm(form: VisitsFormGroup, visits: VisitsFormGroupInput): void {
    const visitsRawValue = this.convertVisitsToVisitsRawValue({ ...this.getFormDefaults(), ...visits });
    form.reset(
      {
        ...visitsRawValue,
        id: { value: visitsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VisitsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      visitdate: currentTime,
    };
  }

  private convertVisitsRawValueToVisits(rawVisits: VisitsFormRawValue | NewVisitsFormRawValue): IVisits | NewVisits {
    return {
      ...rawVisits,
      visitdate: dayjs(rawVisits.visitdate, DATE_TIME_FORMAT),
    };
  }

  private convertVisitsToVisitsRawValue(
    visits: IVisits | (Partial<NewVisits> & VisitsFormDefaults)
  ): VisitsFormRawValue | PartialWithRequiredKeyOf<NewVisitsFormRawValue> {
    return {
      ...visits,
      visitdate: visits.visitdate ? visits.visitdate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
