import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVet, NewVet } from '../vet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVet for edit and NewVetFormGroupInput for create.
 */
type VetFormGroupInput = IVet | PartialWithRequiredKeyOf<NewVet>;

type VetFormDefaults = Pick<NewVet, 'id'>;

type VetFormGroupContent = {
  id: FormControl<IVet['id'] | NewVet['id']>;
  firstname: FormControl<IVet['firstname']>;
  lastname: FormControl<IVet['lastname']>;
};

export type VetFormGroup = FormGroup<VetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VetFormService {
  createVetFormGroup(vet: VetFormGroupInput = { id: null }): VetFormGroup {
    const vetRawValue = {
      ...this.getFormDefaults(),
      ...vet,
    };
    return new FormGroup<VetFormGroupContent>({
      id: new FormControl(
        { value: vetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstname: new FormControl(vetRawValue.firstname, {
        validators: [Validators.required, Validators.maxLength(32)],
      }),
      lastname: new FormControl(vetRawValue.lastname, {
        validators: [Validators.required, Validators.maxLength(32)],
      }),
    });
  }

  getVet(form: VetFormGroup): IVet | NewVet {
    return form.getRawValue() as IVet | NewVet;
  }

  resetForm(form: VetFormGroup, vet: VetFormGroupInput): void {
    const vetRawValue = { ...this.getFormDefaults(), ...vet };
    form.reset(
      {
        ...vetRawValue,
        id: { value: vetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VetFormDefaults {
    return {
      id: null,
    };
  }
}
