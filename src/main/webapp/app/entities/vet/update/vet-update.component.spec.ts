import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VetFormService } from './vet-form.service';
import { VetService } from '../service/vet.service';
import { IVet } from '../vet.model';

import { VetUpdateComponent } from './vet-update.component';

describe('Vet Management Update Component', () => {
  let comp: VetUpdateComponent;
  let fixture: ComponentFixture<VetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vetFormService: VetFormService;
  let vetService: VetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VetUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vetFormService = TestBed.inject(VetFormService);
    vetService = TestBed.inject(VetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vet: IVet = { id: 456 };

      activatedRoute.data = of({ vet });
      comp.ngOnInit();

      expect(comp.vet).toEqual(vet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVet>>();
      const vet = { id: 123 };
      jest.spyOn(vetFormService, 'getVet').mockReturnValue(vet);
      jest.spyOn(vetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vet }));
      saveSubject.complete();

      // THEN
      expect(vetFormService.getVet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vetService.update).toHaveBeenCalledWith(expect.objectContaining(vet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVet>>();
      const vet = { id: 123 };
      jest.spyOn(vetFormService, 'getVet').mockReturnValue({ id: null });
      jest.spyOn(vetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vet }));
      saveSubject.complete();

      // THEN
      expect(vetFormService.getVet).toHaveBeenCalled();
      expect(vetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVet>>();
      const vet = { id: 123 };
      jest.spyOn(vetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
