describe('Login', () => {
  const username = Cypress.env('petClinicUsername');
  const password = Cypress.env('petClinicUsername');

  beforeEach(() => {
    cy.visit('http://localhost:9000/login');
    cy.get('[data-cy="navbar"]').get('[data-cy="accountMenu"]').click().get('[data-cy="login"]').click();
  });

  beforeEach(() => {
    cy.intercept('POST', '/api/authenticate').as('authenticate');
  });

  it('Intento de login con password incorrecto', () => {
    cy.get('[data-cy="username"]').type(username);
    cy.get('[data-cy="password"]').type('bad-password');
    cy.get('[data-cy="submit"]').click();
    cy.wait('@authenticate').then(({ response }) => expect(response.statusCode).to.equal(401));
    cy.get('[data-cy="loginError"]').should('be.visible');
  });

  it('Intento de login con password correcto', () => {
    cy.get('[data-cy="username"]').type(username);
    cy.get('[data-cy="password"]').type(password);
    cy.get('[data-cy="submit"]').click();
    cy.wait('@authenticate').then(({ response }) => expect(response.statusCode).to.equal(200));
    cy.hash().should('eq', '');
  });
});
