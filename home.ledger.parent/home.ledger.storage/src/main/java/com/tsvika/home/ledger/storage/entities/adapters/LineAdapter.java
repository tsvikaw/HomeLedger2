package com.tsvika.home.ledger.storage.entities.adapters;

import com.tsvika.home.ledger.dto.CategoryForUpdate;
import com.tsvika.home.ledger.dto.LineForUpdate;
import com.tsvika.home.ledger.storage.entities.dao.Category;
import com.tsvika.home.ledger.storage.entities.dao.Line;
import com.tsvika.home.ledger.dto.LineDisplayable;
import com.tsvika.home.ledger.dto.LineForCreation;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class LineAdapter implements ILineAdapter {

    @Override
    public LineDisplayable generateLineDisplayable(Line line) {
        LineDisplayable lineDisplayable = new LineDisplayable();
        lineDisplayable.setId(line.getId());
        lineDisplayable.setAccount(line.getAccount());
        lineDisplayable.setAmount(line.getAmount());
        lineDisplayable.setCategoryId(line.getCategory().getId());
        lineDisplayable.setCategoryName(line.getCategory().getName());
        lineDisplayable.setDate(line.getDate());
        lineDisplayable.setDescription(line.getDescription());
        return lineDisplayable;
    }

    @Override
    public Line generateLine(LineForCreation lineForCreation) {
        Line line = new Line();
        line.setId(UUID.randomUUID().toString());
        line.setAccount(lineForCreation.getAccount());
        line.setAmount(lineForCreation.getAmount());
        line.setDate(lineForCreation.getDate());
        line.setDescription(lineForCreation.getDescription());

        return line;
    }

    @Override
        public Line generateLine(LineForUpdate lineForUpdate, Line existing, Category category) {
        existing.setAmount(lineForUpdate.getAmount());
        existing.setDescription(lineForUpdate.getDescription());
        existing.setAccount(lineForUpdate.getAccount());
        existing.setCategory(category);
        return existing;
    }
}
