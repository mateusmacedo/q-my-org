package org.acme.product;

import org.acme.core.edaes.BaseCommand;

public class RegisterProductCommand extends BaseCommand<RegisterProductDto> {

    private RegisterProductCommand() {
    }

    public static BaseCommand<RegisterProductDto> fromDto(RegisterProductDto dto) {
        return new RegisterProductCommand().withData(dto);
    }
}
