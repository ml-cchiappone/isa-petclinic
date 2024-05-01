describe('Logs', () => {
  const username = Cypress.env('petClinicUsername');
  const password = Cypress.env('petClinicUsername');

  beforeEach(() => {
    cy.visit('http://localhost:9000');
  });

  it('Hago click en el item del menú log', () => {
    cy.get('[data-cy="navbar"]').get('[data-cy="adminMenu"]').click().get(`.dropdown-item[href="/admin/logs"]`).click();
    cy.get('[data-cy="logsPageHeading"]').should('be.visible');
  });
});
