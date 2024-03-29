/**
 * Servert input dto.
 */
export type IDto = UserFormIDto | LoginFormIDto | ConnFormIDto;

/**
 * User form dto.
 */
export interface UserFormIDto {
  email: string;
  pwd: string;
  username: string;
  birthDate: string;
  isTermsAccepted: boolean;
}

/**
 * Login form dto.
 */
export interface LoginFormIDto {
  email: string;
  pwd: string;
}

/**
 * Connection form dto.
 */
export type ConnFormIDto = MongoFormIDto | PostgresFormIDto;

/**
 * Mongo connection form dto.
 */
export interface MongoFormIDto {
  name: string;
  hostPort: {
    host: string;
    port: number;
  }[];
  dbAuth: string;
  replicaSet: string;
  user: string;
  pwd: string;
  kind: "MongoFormIDto";
}

/**
 * Postgres connection form dto.
 */
export interface PostgresFormIDto {
  name: string;
  host: string;
  port: number;
  dbName: string;
  user: string;
  pwd: string;
  kind: "PostgresFormIDto";
}
