/// <reference types="cypress" />

describe('Testing if user can buy 4 adult cards', () => {
  before(() => {
    cy.visit('http://localhost:3000/')
  })

  Cypress.config('defaultCommandTimeout', 10000)
  it('user logs in, add 4 adult cards to basket and buys them', () => {
    // prijavim se
    cy.get('h1').should('have.text', 'Prijava')
    cy.get('form').find('input[name="username"]').should('exist').type('marcel')
    cy.get('form').find('input[name="password"]').should('exist').type('marcel')
    cy.contains('button', 'Prijavite se').click()

    cy.contains('Cenik smučarskih vozovnic').should('exist')

    // dodam 4 karte v košarico
    cy.get('div[name="Card_Odrasli"]').find('div[name="CardBody_Odrasli"]').contains('Odrasli').then(() => {
      cy.get('div[name="Card_Odrasli"]').find('div[name="CardFooter_Odrasli"]').contains('button', 'Add').should('exist').then(($button) => {
        for (let i = 0; i < 4; i++) {
          cy.wrap($button).click()
        }
      })
    })

    // opravim nakup
    cy.contains('button', 'Nakup').should('exist').click()

    // prikaže se toast in če je uspešen potem kliknemo na njega
    cy.get('.Toastify').find('.Toastify__toast-body').contains('div', 'Nakup uspešen!').should('be.visible').then(($el) => {
      cy.wrap($el).click()
    })
  })
})