package net.rtxyd.fallen_lib.type.service;

import java.util.List;

public interface IFallenPatchRegistry<Entry> {
    List<Entry> match(String name);
}
