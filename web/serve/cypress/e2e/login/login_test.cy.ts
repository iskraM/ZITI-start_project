/// <reference types="cypress" />

describe('Testing the register and login functionality', () => {
  before(() => {
    cy.visit('http://localhost:3000/')
  })

  it('shows login form and clicks on register button, inputs user data and clicks register button, inputs' +
    'same data into login and comes to home page where he can click logout button', () => {
    const randomNumber = Math.floor(Math.random() * 1000);
    
    // nahajam se na LogIn strani
    cy.get('h1').should('have.text', 'Prijava')
    cy.contains('Registrirajte se').click()

    // na Register strani se registrira novi uporabnik
    cy.get('h1').should('contain.text', 'Registracija')
    cy.get('form').find('input[name="email"]').should('exist').type(`marcel${randomNumber}@gmail.com`)
    cy.get('form').find('input[name="username"]').should('exist').type(`marcel${randomNumber}`)
    cy.get('form').find('input[name="password"]').should('exist').type('marcel')
    cy.contains('button', 'Registrirajte se').click()

    // na LogIn strani se novi uporabnik prijavi
    cy.get('h1').should('have.text', 'Prijava')
    cy.get('form').find('input[name="username"]').should('exist').type(`marcel${randomNumber}`)
    cy.get('form').find('input[name="password"]').should('exist').type('marcel')
    cy.contains('button', 'Prijavite se').click()

    // uporabnik je na main strani in se odjavi
    cy.get('a[href="/profile"]').should('be.visible')
    cy.contains('Logout').click();

    cy.get('h1').should('have.text', 'Prijava')
  })
})