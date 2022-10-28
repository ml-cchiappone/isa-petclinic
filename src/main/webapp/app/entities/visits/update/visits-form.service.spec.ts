import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../visits.test-samples';

import { VisitsFormService } from './visits-form.service';

describe('Visits Form Service', () => {
  let service: VisitsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VisitsFormService);
  });

  describe('Service methods', () => {
    describe('createVisitsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVisitsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            visitdate: expect.any(Object),
            description: expect.any(Object),
            pet: expect.any(Object),
          })
        );
      });

      it('passing IVisits should create a new form with FormGroup', () => {
        const formGroup = service.createVisitsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            visitdate: expect.any(Object),
            description: expect.any(Object),
            pet: expect.any(Object),
          })
        );
      });
    });

    describe('getVisits', () => {
      it('should return NewVisits for default Visits initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVisitsFormGroup(sampleWithNewData);

        const visits = service.getVisits(formGroup) as any;

        expect(visits).toMatchObject(sampleWithNewData);
      });

      it('should return NewVisits for empty Visits initial value', () => {
        const formGroup = service.createVisitsFormGroup();

        const visits = service.getVisits(formGroup) as any;

        expect(visits).toMatchObject({});
      });

      it('should return IVisits', () => {
        const formGroup = service.createVisitsFormGroup(sampleWithRequiredData);

        const visits = service.getVisits(formGroup) as any;

        expect(visits).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVisits should not enable id FormControl', () => {
        const formGroup = service.createVisitsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVisits should disable id FormControl', () => {
        const formGroup = service.createVisitsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
