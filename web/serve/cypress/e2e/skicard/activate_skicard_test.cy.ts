/// <reference types="cypress" />

describe('Testing if user can activate ski cards', () => {
  before(() => {
    cy.visit('http://localhost:3000/')
  })

  it('logs in, goes to profile, and activates first 2 cards with physical card IDs', () => {
    // prijavim se
    cy.get('h1').should('have.text', 'Prijava')
    cy.get('form').find('input[name="username"]').should('exist').type('marcel')
    cy.get('form').find('input[name="password"]').should('exist').type('marcel')
    cy.contains('button', 'Prijavite se').click()

    cy.contains('Cenik smučarskih vozovnic').should('exist')

    // premaknem se na profil in preverim ali imam naročilo
    cy.contains('Profile').should('exist').click()

    // preverim ali imam naročilo
    cy.contains('Vaša naročila:')

    cy.get(':nth-child(4) > .px-3 > .my-auto').click()

    //cy.get('.py-3 > :nth-child(4) > :nth-child(2) > :nth-child(3)').contains('button') //.find('input[name="pyhsical_card_0"]').should('exist')
    cy.get(':nth-child(3) > .gap-6 > .relative > .placeholder\:italic')
    cy.get(':nth-child(4) > :nth-child(2) > :nth-child(4)') //.find('input[name="pyhsical_card_1"]').should('exist')
  })
})