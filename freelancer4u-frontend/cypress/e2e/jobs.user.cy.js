/// <reference types="Cypress"/>

describe("Manage jobs as user", () => {

    // Login first as admin, to clear jobs and create new ones
    before(() => {
        cy.clearAllSessionStorage();

        cy.visit("http://localhost:8080");
        cy.get("#username").type(Cypress.env()["admin"].email);
        cy.get("#password").type(Cypress.env()["admin"].password);
        cy.contains("button", "Log in").click();
        cy.request({
            method: "DELETE",
            url: "http://localhost:8080/api/job",
            headers: {
                Authorization: "Bearer " + sessionStorage.getItem("jwt_token"),
            },
        });
        
        cy.get('a[href="#/jobs"]').click();
        cy.location("hash").should("include", "jobs");
        const formData = [
            { description: 'TestDescription1', type: 'TEST', earnings: 200 },
            { description: 'TestDescription2', type: 'IMPLEMENT', earnings: 100 },
            { description: 'TestDescription3', type: 'REVIEW', earnings: 150 },
            { description: 'TestDescription4', type: 'TEST', earnings: 200 },
            { description: 'TestDescription5', type: 'IMPLEMENT', earnings: 100 },
        ];
        for (const data of formData) {
            cy.get('form').find('#description').type(data.description);
            cy.get('form').find('#type').select(data.type);
            cy.get('form').find('#earnings').type(data.earnings);
            cy.get('form').find('button.btn-primary').click();
            cy.get('form').find('#description').clear();
            cy.get('form').find('#earnings').clear();
        }
        cy.get('#logout').click();
    })


    specify("login as user", () => {
        cy.visit("http://localhost:8080");
        cy.get("#username").type(Cypress.env()["user"].email);
        cy.get("#password").type(Cypress.env()["user"].password);
        cy.contains("button", "Log in").click();
        cy.get("h1").should("contain", "Welcome");
    });


    it("visit jobs page", () => {
        cy.get('a[href="#/jobs"]').click();
        cy.location("hash").should("include", "jobs");
    })


    it("check no create-form", () => {
        cy.get('h1').should('not.contain', 'Create');
    })


    it("check pagination", () => {

        // Page 1
        cy.get('a[href="#/jobs?page=1"]').click();
        cy.get('tbody>tr').should(($tr) => {
            expect($tr).to.have.length(4)
        })

        // Page 2
        cy.get('a[href="#/jobs?page=2"]').click()
        cy.get('tbody>tr').should(($tr) => {
            expect($tr).to.have.length(1)
        })
        cy.get('tbody>tr').should('contain', 'TestDescription5');
    })


    it("check assignToMe", () => {
        cy.get('a[href="#/jobs?page=1"]').click();
        cy.get('tbody>tr').should('contain', 'TestDescription2');
        cy.get('tr:contains("TestDescription1") button:contains("Assign to me")').click();
        cy.get('tr:contains("TestDescription1")').should('contain', 'ASSIGNED');
    });


    it("check completeMyJob", () => {
        cy.get('a[href="#/jobs?page=1"]').click();
        cy.get('tbody>tr').should('contain', 'TestDescription2');
        cy.get('tr:contains("TestDescription1") button:contains("Complete Job")').click();
        cy.get('tr:contains("TestDescription1")').should('contain', 'DONE');
    });


    it("check filter-function", () => {
        cy.get('select#typefilter').select("IMPLEMENT");
        cy.get('div>a.btn.btn-primary:contains("Apply")').click();
        cy.get('tbody>tr').should(($tr) => {
            expect($tr).to.have.length(2)
        })

        // clear Jobtype-Filter
        cy.get('select#typefilter').select("");

        // test minEarnings
        cy.get('#minEarnings').type(150)
        cy.get('div>a.btn.btn-primary:contains("Apply")').click();
        cy.get('tbody>tr').should(($tr) => {
            expect($tr).to.have.length(3)
        })
    })
})