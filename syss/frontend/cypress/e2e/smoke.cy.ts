describe("smoke", () => {
  it("loads home and map page", () => {
    cy.visit("/");
    cy.contains("SYSS COP");
    cy.contains("Map").click();
    cy.url().should("include", "/map");
  });
});