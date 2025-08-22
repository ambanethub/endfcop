# syntax=docker/dockerfile:1.7
FROM node:18.20-alpine AS deps
WORKDIR /app
COPY package.json pnpm-lock.yaml* ./
RUN corepack enable && corepack prepare pnpm@9.9.0 --activate \
	&& pnpm install --frozen-lockfile

FROM node:18.20-alpine AS build
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN corepack enable && corepack prepare pnpm@9.9.0 --activate \
	&& pnpm build

FROM node:18.20-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production
COPY --from=build /app/.next ./.next
COPY --from=build /app/package.json ./package.json
COPY --from=build /app/node_modules ./node_modules
EXPOSE 3000
CMD ["node", "/app/node_modules/next/dist/bin/next", "start", "-p", "3000"]