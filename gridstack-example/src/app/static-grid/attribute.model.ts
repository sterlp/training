import { EventEmitter } from '@angular/core';
import { isUndefined, isNumber, isString } from 'util';
/*
export interface AttributeParser<T> {
    (source: any): T;
}
*/

/**
 * Simple function interface for persers like:
 *
 * @see https://github.com/angular/components/tree/master/src/cdk/coercion
 */
export type AttributeParser<T> = (source: any) => T;
/**
 * Simple class to help the creation of bidi binding attributes.
 * As typescript and angular doesn't enfore types this class helps to handle it.
 *
 * Instead of:
 * ```ts
 * @Output('valueChange') value: EventEmitter<number> = new EventEmitter<number>();
 * @Input('value') value: number = 1; // parsing code removed
 * ```
 *
 * ```ts
 * _value = new Attribute<number>(this.yChange, NUMBER_PARSER, 1);
 * @Output('valueChange') valueChange = _value.eventEmitter;
 * @Input('value') set value(value: number) { this._value.value = value; }
 * get value(): number {return this._value.value; }
 * ```
 *
 * @see https://github.com/angular/components/tree/master/src/cdk/coercion
 */
/* tslint:disable:variable-name no-output-rename no-input-rename curly*/
export class Attribute<T> {
    private _parser: AttributeParser<T>;
    private _event: EventEmitter<T>;
    get eventEmitter(): EventEmitter<T> {return this._event; }

    private _value: T;
    set value(v: T) {
        if (this._value !== v) {
            v = this._parser(v);
            if (this._value !== v) {
                this._value = v;
                this._event.emit(this._value);
            }
        }
    }
    get value(): T {return this._value; }

    constructor(parser: AttributeParser<T>, value?: T) {
        this._event = new EventEmitter<T>();
        this._parser = parser;
        if (!isUndefined(value)) {
            this._value = value;
        }
    }

    complete(): void {
        this._event.complete();
    }

    public toString(): string {
        return typeof this.value + ' ' + this.value;
    }
}

export const NOP_PARSER: AttributeParser<any> = (source) => {
    return source;
};
export const STRING_PARSER: AttributeParser<string> = (source) => {
    if (isUndefined(source)) return null;
    else if (isNumber(source)) return source.toString();
    else if (!isString(source)) return '' + source;
    else return source;
};
/**
 * Converts the given value to a number
 * https://flaviocopes.com/how-to-convert-string-to-number-javascript/
 * @param source input value
 */
export const NUMBER_PARSER: AttributeParser<number> = (source) => {
    if (isUndefined(source)) return null;
    else if (!isNumber(source)) return source * 1;
    else return source;
};
export const INT_PARSER: AttributeParser<number> = (source) => {
    if (isUndefined(source)) return null;
    else return parseInt(source, 10);
};
