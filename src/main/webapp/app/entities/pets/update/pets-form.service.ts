import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPets, NewPets } from '../pets.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPets for edit and NewPetsFormGroupInput for create.
 */
type PetsFormGroupInput = IPets | PartialWithRequiredKeyOf<NewPets>;

type PetsFormDefaults = Pick<NewPets, 'id'>;

type PetsFormGroupContent = {
  id: FormControl<IPets['id'] | NewPets['id']>;
  name: FormControl<IPets['name']>;
  type: FormControl<IPets['type']>;
  human: FormControl<IPets['human']>;
  vet: FormControl<IPets['vet']>;
};

export type PetsFormGroup = FormGroup<PetsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PetsFormService {
  createPetsFormGroup(pets: PetsFormGroupInput = { id: null }): PetsFormGroup {
    const petsRawValue = {
      ...this.getFormDefaults(),
      ...pets,
    };
    return new FormGroup<PetsFormGroupContent>({
      id: new FormControl(
        { value: petsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(petsRawValue.name, {
        validators: [Validators.required, Validators.maxLength(32)],
      }),
      type: new FormControl(petsRawValue.type),
      human: new FormControl(petsRawValue.human),
      vet: new FormControl(petsRawValue.vet),
    });
  }

  getPets(form: PetsFormGroup): IPets | NewPets {
    return form.getRawValue() as IPets | NewPets;
  }

  resetForm(form: PetsFormGroup, pets: PetsFormGroupInput): void {
    const petsRawValue = { ...this.getFormDefaults(), ...pets };
    form.reset(
      {
        ...petsRawValue,
        id: { value: petsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PetsFormDefaults {
    return {
      id: null,
    };
  }
}
