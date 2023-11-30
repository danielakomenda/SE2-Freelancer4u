describe('Login Page', () => { it('should show login page', () => {
    cy.clearAllSessionStorage(); cy.visit('http://localhost:8080'); cy.get('.card-header').should('contain', 'Login');
}) })