package com.pengu.divination.core.entity.npc.home;

import java.util.HashSet;
import java.util.Set;

public class House
{
	public Set<Long> exits = new HashSet<>();
	public Set<Long> lights = new HashSet<>();
	public Set<Long> tables = new HashSet<>();
}