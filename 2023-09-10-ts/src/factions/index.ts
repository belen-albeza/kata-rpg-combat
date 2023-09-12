export class FactionDoesNotExistError extends Error {}
export class MemberDoesNotBelongError extends Error {}

export interface FactionJoiner {}

export class FactionManager {
  #factions = new Map<string, Set<FactionJoiner>>();

  add(id: string) {
    this.#factions.set(id, new Set());
  }

  join(factionId: string, member: FactionJoiner) {
    const faction = this.#faction(factionId);
    faction.add(member);
  }

  leave(factionId: string, member: FactionJoiner) {
    const faction = this.#faction(factionId);
    const wasDeleted = faction.delete(member);
    if (!wasDeleted) {
      throw new MemberDoesNotBelongError(
        `member ${member} does not belong to faction ${factionId}`
      );
    }
  }

  hasMember(factionId: string, member: FactionJoiner): boolean {
    const faction = this.#faction(factionId);
    return faction.has(member);
  }

  areAllies(member: FactionJoiner, other: FactionJoiner): boolean {
    if (member === other) {
      return true;
    }

    const memberships = this.#membershipsFor(member);
    const otherMemberships = this.#membershipsFor(other);

    const common = memberships.filter((x) => otherMemberships.includes(x));

    return common.length > 0;
  }

  #faction(id: string): Set<FactionJoiner> {
    const faction = this.#factions.get(id);
    if (!faction) {
      throw new FactionDoesNotExistError(`faction ${id} does not exist`);
    }

    return faction;
  }

  get factions(): string[] {
    return [...this.#factions.keys()];
  }

  #membershipsFor(member: FactionJoiner): string[] {
    return [...this.#factions.entries()]
      .filter(([_, members]) => members.has(member))
      .map(([key, _]) => key);
  }
}
