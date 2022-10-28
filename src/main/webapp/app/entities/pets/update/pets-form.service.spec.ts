import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pets.test-samples';

import { PetsFormService } from './pets-form.service';

describe('Pets Form Service', () => {
  let service: PetsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PetsFormService);
  });

  describe('Service methods', () => {
    describe('createPetsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPetsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            human: expect.any(Object),
            vet: expect.any(Object),
          })
        );
      });

      it('passing IPets should create a new form with FormGroup', () => {
        const formGroup = service.createPetsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            human: expect.any(Object),
            vet: expect.any(Object),
          })
        );
      });
    });

    describe('getPets', () => {
      it('should return NewPets for default Pets initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPetsFormGroup(sampleWithNewData);

        const pets = service.getPets(formGroup) as any;

        expect(pets).toMatchObject(sampleWithNewData);
      });

      it('should return NewPets for empty Pets initial value', () => {
        const formGroup = service.createPetsFormGroup();

        const pets = service.getPets(formGroup) as any;

        expect(pets).toMatchObject({});
      });

      it('should return IPets', () => {
        const formGroup = service.createPetsFormGroup(sampleWithRequiredData);

        const pets = service.getPets(formGroup) as any;

        expect(pets).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPets should not enable id FormControl', () => {
        const formGroup = service.createPetsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPets should disable id FormControl', () => {
        const formGroup = service.createPetsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
