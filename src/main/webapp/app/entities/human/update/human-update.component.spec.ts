import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HumanFormService } from './human-form.service';
import { HumanService } from '../service/human.service';
import { IHuman } from '../human.model';

import { HumanUpdateComponent } from './human-update.component';

describe('Human Management Update Component', () => {
  let comp: HumanUpdateComponent;
  let fixture: ComponentFixture<HumanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let humanFormService: HumanFormService;
  let humanService: HumanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HumanUpdateComponent],
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
      .overrideTemplate(HumanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HumanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    humanFormService = TestBed.inject(HumanFormService);
    humanService = TestBed.inject(HumanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const human: IHuman = { id: 456 };

      activatedRoute.data = of({ human });
      comp.ngOnInit();

      expect(comp.human).toEqual(human);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHuman>>();
      const human = { id: 123 };
      jest.spyOn(humanFormService, 'getHuman').mockReturnValue(human);
      jest.spyOn(humanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ human });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: human }));
      saveSubject.complete();

      // THEN
      expect(humanFormService.getHuman).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(humanService.update).toHaveBeenCalledWith(expect.objectContaining(human));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHuman>>();
      const human = { id: 123 };
      jest.spyOn(humanFormService, 'getHuman').mockReturnValue({ id: null });
      jest.spyOn(humanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ human: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: human }));
      saveSubject.complete();

      // THEN
      expect(humanFormService.getHuman).toHaveBeenCalled();
      expect(humanService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHuman>>();
      const human = { id: 123 };
      jest.spyOn(humanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ human });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(humanService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
