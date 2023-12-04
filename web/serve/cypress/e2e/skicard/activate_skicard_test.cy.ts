/// <reference types="cypress" />

describe('Testing if user can activate ski cards', () => {
  before(() => {
    cy.visit('http://localhost:3000/')
  })

  Cypress.config('defaultCommandTimeout', 10000)
  it('logs in, goes to profile, and activates first 2 cards with physical card IDs', () => {
    // prijavim se
    cy.get('h1').should('have.text', 'Prijava')
    cy.get('form').find('input[name="username"]').should('exist').type('marcel')
    cy.get('form').find('input[name="password"]').should('exist').type('marcel')
    cy.contains('button', 'Prijavite se').click()

    cy.contains('Cenik smučarskih vozovnic').should('exist')

    // premaknem se na profil in preverim ali imam naročilo
    cy.contains('Profile').should('exist').click()

    // preverim ali imam naročilo in vsebuje 4 karte
    cy.contains('Vaša naročila:')
    cy.get(':nth-child(4) > .px-3 > .my-auto').click()
    cy.get('.py-3 > :nth-child(4)').find('input').should('have.length', 4)

    // aktiviram prvo karto
    cy.get('.py-3 > :nth-child(4) > :nth-child(2) > :nth-child(3)').then(($parent) => {
      cy.wrap($parent).find('input').should('exist').type('USH-TE0-836')
      cy.wrap($parent).find('button').should('exist').click()
    })

    // prikaže se toast in če je uspešen potem kliknemo na njega
    cy.get('.Toastify').find('.Toastify__toast-body').contains('div', 'Karta je bila uspešno aktivirana!').should('be.visible').then(($el) => {
      cy.wrap($el).click()      
    })

    // aktiviram drugo karto
    cy.get(':nth-child(4) > :nth-child(2) > :nth-child(4)').then(($parent) => {
      cy.wrap($parent).find('input').should('exist').type('TXU-BWP-804')
      cy.wrap($parent).find('button').should('exist').click()
    })

    // prikaže se toast in če je uspešen potem kliknemo na njega
    cy.get('.Toastify').find('.Toastify__toast-body').contains('div', 'Karta je bila uspešno aktivirana!').should('be.visible').then(($el) => {
      cy.wrap($el).click()      
    })
  })
})