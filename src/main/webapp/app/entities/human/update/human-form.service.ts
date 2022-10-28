import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHuman, NewHuman } from '../human.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHuman for edit and NewHumanFormGroupInput for create.
 */
type HumanFormGroupInput = IHuman | PartialWithRequiredKeyOf<NewHuman>;

type HumanFormDefaults = Pick<NewHuman, 'id'>;

type HumanFormGroupContent = {
  id: FormControl<IHuman['id'] | NewHuman['id']>;
  firstname: FormControl<IHuman['firstname']>;
  lastname: FormControl<IHuman['lastname']>;
  address: FormControl<IHuman['address']>;
  city: FormControl<IHuman['city']>;
  telephone: FormControl<IHuman['telephone']>;
};

export type HumanFormGroup = FormGroup<HumanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HumanFormService {
  createHumanFormGroup(human: HumanFormGroupInput = { id: null }): HumanFormGroup {
    const humanRawValue = {
      ...this.getFormDefaults(),
      ...human,
    };
    return new FormGroup<HumanFormGroupContent>({
      id: new FormControl(
        { value: humanRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstname: new FormControl(humanRawValue.firstname, {
        validators: [Validators.required, Validators.maxLength(32)],
      }),
      lastname: new FormControl(humanRawValue.lastname, {
        validators: [Validators.required, Validators.maxLength(32)],
      }),
      address: new FormControl(humanRawValue.address, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      city: new FormControl(humanRawValue.city, {
        validators: [Validators.maxLength(32)],
      }),
      telephone: new FormControl(humanRawValue.telephone, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
    });
  }

  getHuman(form: HumanFormGroup): IHuman | NewHuman {
    return form.getRawValue() as IHuman | NewHuman;
  }

  resetForm(form: HumanFormGroup, human: HumanFormGroupInput): void {
    const humanRawValue = { ...this.getFormDefaults(), ...human };
    form.reset(
      {
        ...humanRawValue,
        id: { value: humanRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HumanFormDefaults {
    return {
      id: null,
    };
  }
}
