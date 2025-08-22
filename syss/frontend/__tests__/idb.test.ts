import { describe, it, expect } from "vitest";
import { getDB } from "@/lib/idb";

describe("idb setup", () => {
  it("opens database", async () => {
    const db = await getDB();
    expect(db.name).toBe("syss");
  });
});