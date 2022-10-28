import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Human e2e test', () => {
  const humanPageUrl = '/human';
  const humanPageUrlPattern = new RegExp('/human(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const humanSample = {
    firstname: 'reboot Armenian Investigación',
    lastname: 'whiteboard Sorprendente Estrateg',
    address: 'IB Dinánmico front-end',
    telephone: '900-257-354',
  };

  let human;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/humans+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/humans').as('postEntityRequest');
    cy.intercept('DELETE', '/api/humans/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (human) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/humans/${human.id}`,
      }).then(() => {
        human = undefined;
      });
    }
  });

  it('Humans menu should load Humans page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('human');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Human').should('exist');
    cy.url().should('match', humanPageUrlPattern);
  });

  describe('Human page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(humanPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Human page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/human/new$'));
        cy.getEntityCreateUpdateHeading('Human');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', humanPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/humans',
          body: humanSample,
        }).then(({ body }) => {
          human = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/humans+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/humans?page=0&size=20>; rel="last",<http://localhost/api/humans?page=0&size=20>; rel="first"',
              },
              body: [human],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(humanPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Human page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('human');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', humanPageUrlPattern);
      });

      it('edit button click should load edit Human page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Human');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', humanPageUrlPattern);
      });

      it('edit button click should load edit Human page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Human');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', humanPageUrlPattern);
      });

      it('last delete button click should delete instance of Human', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('human').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', humanPageUrlPattern);

        human = undefined;
      });
    });
  });

  describe('new Human page', () => {
    beforeEach(() => {
      cy.visit(`${humanPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Human');
    });

    it('should create an instance of Human', () => {
      cy.get(`[data-cy="firstname"]`).type('contingencia').should('have.value', 'contingencia');

      cy.get(`[data-cy="lastname"]`).type('Libyan withdrawal').should('have.value', 'Libyan withdrawal');

      cy.get(`[data-cy="address"]`).type('intangible').should('have.value', 'intangible');

      cy.get(`[data-cy="city"]`).type('Avilésville').should('have.value', 'Avilésville');

      cy.get(`[data-cy="telephone"]`).type('912.534.928').should('have.value', '912.534.928');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        human = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', humanPageUrlPattern);
    });
  });
});
