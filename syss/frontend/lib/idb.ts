import { openDB, DBSchema, IDBPDatabase } from "idb";

interface SyssDB extends DBSchema {
  operations: { key: string; value: any };
  tasks: { key: string; value: any };
  positions: { key: string; value: any };
  messages: { key: string; value: any };
  mutations: { key: string; value: any };
}

let dbPromise: Promise<IDBPDatabase<SyssDB>> | null = null;

export function getDB() {
  if (!dbPromise) {
    dbPromise = openDB<SyssDB>("syss", 1, {
      upgrade(db, oldVersion) {
        if (oldVersion < 1) {
          db.createObjectStore("operations");
          db.createObjectStore("tasks");
          db.createObjectStore("positions");
          db.createObjectStore("messages");
          db.createObjectStore("mutations");
        }
      }
    });
  }
  return dbPromise;
}

function validateKey(key: unknown): string {
  if (typeof key === "string" || typeof key === "number") return String(key);
  throw new Error("Invalid IDB key; expected string|number");
}

export async function idbPut(store: keyof SyssDB, key: unknown, value: any) {
  try {
    const k = validateKey(key);
    const db = await getDB();
    await db.put(store as any, value, k);
  } catch (err) {
    console.error("idbPut error", { store, key }, err);
    throw err;
  }
}

export async function idbGet(store: keyof SyssDB, key: unknown) {
  try {
    const k = validateKey(key);
    const db = await getDB();
    return await db.get(store as any, k);
  } catch (err) {
    console.error("idbGet error", { store, key }, err);
    throw err;
  }
}

export async function idbGetAll(store: keyof SyssDB) {
  try {
    const db = await getDB();
    return await db.getAll(store as any);
  } catch (err) {
    console.error("idbGetAll error", { store }, err);
    throw err;
  }
}

export async function queueMutation(mutation: any) {
  try {
    const db = await getDB();
    const id = `${Date.now()}-${Math.random().toString(36).slice(2)}`;
    await db.put("mutations", mutation, id);
    return id;
  } catch (err) {
    console.error("queueMutation error", err);
    throw err;
  }
}