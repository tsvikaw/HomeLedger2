package com.tsvika.home.ledger.storage.entities.adapters;

import com.tsvika.home.ledger.dto.LineForUpdate;
import com.tsvika.home.ledger.storage.entities.dao.Category;
import com.tsvika.home.ledger.storage.entities.dao.Line;
import com.tsvika.home.ledger.dto.LineDisplayable;
import com.tsvika.home.ledger.dto.LineForCreation;

public interface ILineAdapter {
    LineDisplayable generateLineDisplayable(Line line);
    Line generateLine(LineForCreation lineForCreation);
    Line generateLine(LineForUpdate lineForUpdate, Line existing, Category category);
}
