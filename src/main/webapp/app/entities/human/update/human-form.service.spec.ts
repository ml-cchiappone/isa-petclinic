import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../human.test-samples';

import { HumanFormService } from './human-form.service';

describe('Human Form Service', () => {
  let service: HumanFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HumanFormService);
  });

  describe('Service methods', () => {
    describe('createHumanFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHumanFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            telephone: expect.any(Object),
          })
        );
      });

      it('passing IHuman should create a new form with FormGroup', () => {
        const formGroup = service.createHumanFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            telephone: expect.any(Object),
          })
        );
      });
    });

    describe('getHuman', () => {
      it('should return NewHuman for default Human initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHumanFormGroup(sampleWithNewData);

        const human = service.getHuman(formGroup) as any;

        expect(human).toMatchObject(sampleWithNewData);
      });

      it('should return NewHuman for empty Human initial value', () => {
        const formGroup = service.createHumanFormGroup();

        const human = service.getHuman(formGroup) as any;

        expect(human).toMatchObject({});
      });

      it('should return IHuman', () => {
        const formGroup = service.createHumanFormGroup(sampleWithRequiredData);

        const human = service.getHuman(formGroup) as any;

        expect(human).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHuman should not enable id FormControl', () => {
        const formGroup = service.createHumanFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHuman should disable id FormControl', () => {
        const formGroup = service.createHumanFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
